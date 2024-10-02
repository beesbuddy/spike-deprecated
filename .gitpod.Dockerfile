# Base image
FROM gitpod/workspace-full

# Install Clojure CLI tools (version 1.12)
RUN curl -O https://download.clojure.org/install/linux-install-1.12.0.839.sh && \
    chmod +x linux-install-1.12.0.839.sh && \
    sudo ./linux-install-1.12.0.839.sh && \
    rm linux-install-1.12.0.839.sh

# Install PostgreSQL client
RUN sudo apt-get update && \
    sudo apt-get install -y postgresql-client

# Set environment variables for PostgreSQL
ENV PGHOST=localhost
ENV PGUSER=spike
ENV PGPASSWORD=spike
ENV PGDATABASE=spike

# Expose PostgreSQL port
EXPOSE 5432

# Set up PostgreSQL
USER spike
RUN sudo service postgresql start && \
    psql -c "CREATE USER spike WITH PASSWORD 'spike';" && \
    psql -c "CREATE DATABASE spike OWNER spike;"

# Set up Clojure project dependencies
COPY . /workspace
WORKDIR /workspace
RUN clojure -P

# Set up entrypoint
ENTRYPOINT ["clojure", "-M:repl"]