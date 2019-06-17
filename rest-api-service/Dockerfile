FROM node:11-alpine

COPY index.js .
COPY package.json .
COPY package-lock.json .

RUN npm install 

ENTRYPOINT [ "node", "index.js" ]