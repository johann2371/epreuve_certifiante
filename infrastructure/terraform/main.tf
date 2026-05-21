terraform {
  backend "s3" {
    bucket = "digitrans-terraform-state-johann"
    key    = "prod/terraform.tfstate"
    region = "eu-west-3"
  }
}

provider "aws" {
  region = var.aws_region
}

# VPC par défaut (pour rester simple et gratuit)
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# Security Group pour autoriser le trafic vers la Gateway (8080) et SSH (22)
resource "aws_security_group" "digitrans_sgeg" {
  name        = "digitrans_sg_${var.environment}"
  description = "Security group for DIGITRANS-CM ${var.environment}"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "SSH access"
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "API Gateway access"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Environment = var.environment
  }
}

# Recherche de l'Amazon Machine Image (AMI) Ubuntu gratuite
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"] # Canonical

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

# Instance EC2 (Gratuite si t2.micro ou t3.micro selon région)
resource "aws_instance" "digitrans_server" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance_type
  subnet_id     = data.aws_subnets.default.ids[0]
  vpc_security_group_ids = [aws_security_group.digitrans_sgeg.id]

  # User data pour installer Docker et déployer l'application
  user_data = <<-EOF
              #!/bin/bash
              apt-get update
              apt-get install -y docker.io docker-compose
              systemctl start docker
              systemctl enable docker
              usermod -aG docker ubuntu
              
              # Création du répertoire de l'application
              mkdir -p /app
              cd /app
              
              # Génération du docker-compose.yml de production
              cat << 'DOCKERCOMPOSE' > docker-compose.yml
              version: '3.8'
              services:
                postgres:
                  image: postgres:15-alpine
                  container_name: postgres
                  environment:
                    POSTGRES_USER: postgres
                    POSTGRES_PASSWORD: 1234
                  ports:
                    - "5432:5432"
                  volumes:
                    - postgres_data:/var/lib/postgresql/data
                  networks:
                    - digitrans_net

                api-gateway:
                  image: johannbrandon/api-gateway:latest
                  container_name: api-gateway
                  ports:
                    - "8080:8080"
                  networks:
                    - digitrans_net
                  depends_on:
                    - erp-service
                    - crm-service
                    - supply-chain-service
                    - bi-service

                erp-service:
                  image: johannbrandon/erp-service:latest
                  container_name: erp-service
                  environment:
                    - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/erp_db
                  ports:
                    - "8081:8081"
                  networks:
                    - digitrans_net
                  depends_on:
                    - postgres

                crm-service:
                  image: johannbrandon/crm-service:latest
                  container_name: crm-service
                  environment:
                    - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/crm_db
                  ports:
                    - "8082:8082"
                  networks:
                    - digitrans_net
                  depends_on:
                    - postgres

                supply-chain-service:
                  image: johannbrandon/supply-chain-service:latest
                  container_name: supply-chain-service
                  environment:
                    - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/supply_db
                  ports:
                    - "8083:8083"
                  networks:
                    - digitrans_net
                  depends_on:
                    - postgres

                bi-service:
                  image: johannbrandon/bi-service:latest
                  container_name: bi-service
                  environment:
                    - ERP_SERVICE_URL=http://erp-service:8081
                    - CRM_SERVICE_URL=http://crm-service:8082
                    - SUPPLY_SERVICE_URL=http://supply-chain-service:8083
                  ports:
                    - "8084:8084"
                  networks:
                    - digitrans_net
                  depends_on:
                    - erp-service
                    - crm-service
                    - supply-chain-service

              networks:
                digitrans_net:
                  driver: bridge
              volumes:
                postgres_data:
              DOCKERCOMPOSE
              
              # Démarrage de l'application
              docker-compose pull
              docker-compose up -d
              EOF

  tags = {
    Name        = "Digitrans-Server-${var.environment}"
    Environment = var.environment
    Project     = "DIGITRANS-CM"
  }
}

output "public_ip" {
  value = aws_instance.digitrans_server.public_ip
  description = "L'adresse IP publique de votre serveur pour tester l'API Gateway"
}
