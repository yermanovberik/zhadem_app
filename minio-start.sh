#!/bin/bash

# Set up environment variables
export MINIO_ACCESS_KEY="minioadmin"
export MINIO_SECRET_KEY="minioadmin"

# Start MinIO server
minio server /data