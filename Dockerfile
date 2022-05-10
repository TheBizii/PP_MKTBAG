FROM php:8.1-cli
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
CMD [ "vendor/bin/heroku-php-apache2", "public/" ]
