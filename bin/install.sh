mkdir -p /etc/sj/
curl -L https://github.com/lawrenceching/sj/releases/download/v0.0.1/sj.jar -o /etc/sj/sj.jar
curl -L https://raw.githubusercontent.com/lawrenceching/sj/main/bin/sj > /usr/bin/sj
chmod +x /usr/bin/sj