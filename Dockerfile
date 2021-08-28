FROM selenium/standalone-chrome:4.0.0-rc-1-prerelease-20210823

RUN apt-get update && apt-get install -y screen mc htop
