# Base image
FROM gitpod/workspace-postgres

# Install Java 17, rlwrap
RUN sudo apt-get update && \
    sudo apt-get install -y zsh openjdk-17-jdk rlwrap \
    curl -fsSL https://deb.nodesource.com/setup_current.x | sudo -E bash - && \
    sudo apt-get install -y nodejs && \
    sudo ln -s /usr/bin/nodejs /usr/bin/node


# Install Clojure CLI tools (version latest)
RUN curl -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh && \
    chmod +x linux-install.sh && \
    sudo ./linux-install.sh && \
    rm linux-install.sh

# Set up Clojure project dependencies
COPY . /workspace
WORKDIR /workspace
RUN clojure -P
