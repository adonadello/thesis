git pull
mvn package
nohup java -Dconfig.file=conf/application.conf -cp target/THESIS-0.0.1-SNAPSHOT-jar-with-dependencies.jar it.utiu.thesis.Runner
