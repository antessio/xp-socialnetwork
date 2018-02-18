
# xp-socialnetwork
A very simple command-line based social network

### Requirements
- java: version >= 1.8.0_92
- maven: version >= 3.2.5

### Build
Move into the project root folder, then type:

    mvn clean package

### Run

    java -cp 'target/xp-socialnetwork.jar:target/classes/*' it.antessio.xpsocialnetwork.main.Main


### Usage

Submit a Post

    <user name> -> <message>

Reading user's posts

    <user name>

Following a user

    <user name> follows <another user>


Reading user's wall

    <user name> wall

