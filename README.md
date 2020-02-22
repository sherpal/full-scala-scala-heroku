# Full Stack Scala on Heroku

This is an example of a full stack Scala application deployed on
Heroku. See this [blog post](https://medium.com/@antoine.doeraene/deploying-a-full-stack-scala-application-on-heroku-6d8093a913b3).

## Stack

The backend uses the Play framework, together with Slick to access a
postgres database.

The frontend uses the Laminar framework for UI rendering.

## Deploy

The app is hosted on Heroku. In order to deploy,
```
# Login to Heroku
heroku login

# Make sure to use the same name as in project/BackendSettings.scala
heroku apps:create full-stack-scala-example --region eu
heroku addons:create heroku-postgresql:hobby-dev

# Create a secret for the Play application
heroku config:set APPLICATION_SECRET=mycoolsecret

# Optional
export SBT_OPTS="-Xmx6G -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=6G -Xss2M  -Duser.timezone=GMT"

sbt clean stage backend/deployHeroku
```

To see the files on the deployed server:
```
heroku run bash --app APPNAME
```
