# GA 應用小型範例

## 單機版執行方式

```java
mvn clean package exec:java@local 
```

## Client-Server 版安裝方式

### 預備工作 (以 Ubuntu 20.04 LTS 為例)

1. 安裝 Java Developement Kit (JDK) 及 Apache Maven
2. 將本 repository clone 至雲端機器之 `/var/lib` 下

```bash
cd /var/lib
git clone https://github.com/ditp-nctu/GADemo_Being
```

3. 若有防火牆請開啟 port 80 & 8001

```bash
sudo ufw allow 80
sudo ufw allow 8001
```

### 啟動 GA Server

編譯並啟動 GA Server

```java
mvn clean package exec:java@server
```

### 安裝 p5.js client webpage

安裝 web server (以 Apache 為例)

```bash
sudo apt install apache2
```

在 web root 下建立連結至 `/var/lib/GADemo_Being/p5js` folder

```bash
sudo ln -s /var/lib/GADemo_Being/p5js /var/www/html/p5js
```

現在就可以在瀏覽器打開佈署完成的網頁：

```url
http://{server ip address}/p5js
```

### 執行畫面範例

![GABeing](processing/GABeing.png "GA 應用小型範例執行畫面")
