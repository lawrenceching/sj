# sj - Run Java as script

## Get Started

### Requirement

Java 21

### Install sj

```bash
sudo curl -L https://raw.githubusercontent.com/lawrenceching/sj/main/bin/install.sh | bash
```

### Write Script

Create a .sj file

```java
#!/usr/bin/env sj
import static me.imlc.Sj.*;

public class Hello {
    public static void main(String[] args) {
        var dir = sh("pwd");
        println("Current location: %s".formatted(dir));
    }
}
```

And then:
```bash
chmod +x Hello.sj
./Hello.sj
```

## Methods

### sh - run bash commands
### password - as user to type a password (TODO)
### progress - show a progress bar (TODO)
