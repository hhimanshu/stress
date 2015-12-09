How to run?
--

```
$ rm -rf out.log; mvn clean install; nohup java -Xmx5g -cp target/stress-1.0-SNAPSHOT.jar:target/lib  Main > out.log &
$ tail -f out.log
```