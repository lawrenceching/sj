mdkir -p /etc/sj/
curl -O https://github.com/lawrenceching/sj/releases/download/v0.0.1/sj-1.0-SNAPSHOT.jar --output-dir /etc/sj/
curl https://raw.githubusercontent.com/lawrenceching/sj/main/bin/sj > /usr/bin/sj
chmod +x sj