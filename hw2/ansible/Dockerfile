FROM alpine:3.11

RUN apk add --no-cache openssh-client ansible git sshpass docker-cli sudo

WORKDIR /ansible

CMD ["ansible-playbook", "-i", "inventories/servers.yml", "playbook.yml"]
