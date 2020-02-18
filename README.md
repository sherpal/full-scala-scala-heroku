# Full Stack Scala on Heroku

This is an example of a full stack Scala application deployed on
Heroku.

## Stack

The backend uses the Play framework, together with Slick to access a
postgres database.

The frontend uses the Laminar framework for UI rendering.

## Deploy

The app is hosted on Heroku. In order to deploy, after logged into
Heroku (using the command `heroku login`), simply type in:
```
export SBT_OPTS="-Xmx6G -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=6G -Xss2M  -Duser.timezone=GMT"
sbt clean stage backend/deployHeroku
```

To see the files on the deployed server:
```
heroku run bash --app APPNAME
```
