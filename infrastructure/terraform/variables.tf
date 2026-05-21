variable "aws_region" {
  description = "The AWS region to deploy to"
  default     = "af-south-1"
}

variable "environment" {
  description = "Environment name (dev, test, prod)"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type (t3.micro for free tier)"
  type        = string
  default     = "t3.micro"
}
