FROM nginx:1.17-alpine

RUN apk add openssl
RUN openssl req -x509 -newkey rsa:4096 -keyout /etc/ssl/private/key.pem -out /etc/ssl/private/cert.pem -days 3650 -passout pass:xxxx -subj '/CN=localhost'

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
COPY keys.pass /etc/ssl/private/keys.pass
