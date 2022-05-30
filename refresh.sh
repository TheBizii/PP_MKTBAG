#!/usr/bin/bash

sudo heroku container:push web && sudo heroku container:release web && sudo heroku logs --tail
