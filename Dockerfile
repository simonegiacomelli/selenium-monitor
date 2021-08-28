FROM selenium/standalone-chrome:4.0.0-rc-1-prerelease-20210823

RUN sudo apt-get update && sudo apt-get install -y screen mc htop
