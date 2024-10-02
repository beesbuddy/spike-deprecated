# Base image
FROM gitpod/workspace-full

# Install Java 17, rlwrap
RUN sudo apt-get update && \
    sudo apt-get install -y openjdk-17-jdk rlwrap

# Install Clojure CLI tools (version latest)
RUN curl -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh && \
    chmod +x linux-install.sh && \
    sudo ./linux-install.sh && \
    rm linux-install.sh

# Install PostgreSQL client
RUN sudo apt-get update && \
    sudo apt-get install -y postgresql-client

# Set environment variables for PostgreSQL
ENV PGHOST=localhost
ENV PGUSER=gitpod
ENV PGPASSWORD=gitpod
ENV PGDATABASE=spike

# Expose PostgreSQL port
EXPOSE 5432

# Set up PostgreSQL
USER gitpod
RUN sudo service postgresql start && \
    psql -c "CREATE USER gitpod WITH PASSWORD 'gitpod';" && \
    psql -c "CREATE DATABASE spike OWNER gitpod;"

# Set up Clojure project dependencies
COPY . /workspace
WORKDIR /workspace
RUN clojure -P

# Set up entrypoint
ENTRYPOINT ["clojure", "-M:repl"]