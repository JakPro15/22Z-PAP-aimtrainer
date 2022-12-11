## PROJEKT PAP-22Z

### Tematyka
Celem projektu jest stworzenie prostego programu testującego celność, refleks i
inne mechaniki obsługi myszki. Program będzie zawierać kilka różnych treningów
obejmujących inne aspekty przecyzyjnego i szybkiego kierowania myszką. Wyniki
tych treningów będą zapisywane w bazie danych, w której dane będą dzielone
pomiędzy różnymi użytkownikami dzięki prostemu systemowi profilów. Każdą z
minigier będzie można dostosować w menu ustawień w celu zmiany poziomu
trudności.

### Skład zespołu:
* Krzysztof Pałucki
* Kamil Michalak
* Jakub Proboszcz
* Paweł Kochański.

### Zastosowane technologie:
* Java
* JavaFX
* Maven
* Hibernate
* Lombok
* MySQL.

### Instalacja i uruchomienie
* Należy zainstalować Javę w wersji co najmniej 17, np. komendą `sudo apt-get install openjdk-17-jre`
oraz OpenJFX, np. komendą `sudo apt-get install openjfx`
* Należy pobrać MySQL ze strony https://dev.mysql.com/downloads/mysql/ (wariant Linux Generic).  
  Otrzymane archiwum należy zapisać w katalogu ~/pap22Z_z03_mysql pod nazwą mysql.tar.xz  
    Bezpośredni link do pobrania:  
        wersja 32-bitowa: https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.31-linux-glibc2.12-i686.tar.xz  
        wersja 64-bitowa: https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.31-linux-glibc2.12-x86_64.tar.xz  
    Można to osiągnąć z linii poleceń następującą komendą:  
        wersja 32-bitowa:  
`mkdir ~/pap22Z_z03_mysql && wget https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.31-linux-glibc2.12-i686.tar.xz -O ~/pap22Z_z03_mysql/mysql.tar.xz`  
        wersja 64-bitowa:  
`mkdir ~/pap22Z_z03_mysql && wget https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.31-linux-glibc2.12-x86_64.tar.xz -O ~/pap22Z_z03_mysql/mysql.tar.xz`  
* Należy uruchomić skrypt instalacyjny `pap22Z_z03/install_and_run.sh`. Jeżeli wyświetlony zostanie napis "Error
while loading shared libraries", należy brakujące biblioteki doinstalować i uruchomić skrypt ponownie. Potrzebna
może się okazać instalacja pakietów libnuma1, libtinfo5, libaio1 lub innych.
* Następne uruchomienia aplikacji należy wykonywać przez skrypt `pap22Z_z03/run.sh`

### Deinstalacja
* W celu usunięcia programu z komputera należy usunąć katalog ~/pap22Z_z03_mysql oraz sklonowane repozytorium.
