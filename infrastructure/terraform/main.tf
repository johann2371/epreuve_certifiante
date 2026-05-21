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
resource "aws_security_group" "digitrans_sg" {
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
  vpc_security_group_ids = [aws_security_group.digitrans_sg.id]

  # User data pour installer Docker et Docker Compose automatiquement
  user_data = <<-EOF
              #!/bin/bash
              apt-get update
              apt-get install -y docker.io docker-compose
              systemctl start docker
              systemctl enable docker
              usermod -aG docker ubuntu
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
