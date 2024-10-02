#!/bin/bash

# Log into PostgreSQL and execute the following commands
psql <<EOF
-- Create the database with the owner
CREATE DATABASE spike_db OWNER gitpod;
EOF