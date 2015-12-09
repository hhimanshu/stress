How to run?
--

* Set the ulimit of your machine to low number
```
$ ulimit -n
2048
```

* Run
```
$ rm -rf out.log; mvn clean install; nohup java -Xmx5g -cp target/stress-1.0-SNAPSHOT.jar:target/lib  Main > out.log &
$ tail -f out.log
```