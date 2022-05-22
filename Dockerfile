FROM php:8.1-cli
RUN apt-get update -y && apt-get install -y libmcrypt-dev zip unzip && pecl install mongodb && cp /usr/local/etc/php/php.ini-production /usr/local/etc/php/php.ini && echo "extension=mongodb.so" >> /usr/local/etc/php/php.ini && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer
#RUN docker-php-ext-install pdo mbstring

WORKDIR /app
COPY . /app

#RUN php -i
RUN composer install

EXPOSE 8000
CMD php artisan serve --host=0.0.0.0 --port=8000
#CMD web $(composer config bin-dir)/heroku-php-apache2 public/
