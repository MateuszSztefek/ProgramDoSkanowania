# ProgramDoSkanowania
Program służący do automatyzacji sortowania i liczenia wymieszanych produktów na dwa zamówienia.<br>
W katalogu **src** znajduje się kod źródłowy, a w pliku **pom.xml** plik konfiguracyjny Maven


## Opis działania
W pliku tekstowym znajdują się dane w formacie KodKreskowy;NazwaZamówienia;DeklarowanaIlość:<br>
4058823081665;APRIL;35<br>
4058823082792;JUNE;26<br>
4058823082822;JUNE;28<br>

Podczas pracy z programem pracownik operuje skanerem. Skanując dany produkt program zlicza ile już takich produktów było i na jakie zamówinie produkt jest przeznaczony. Program pobiera dane ze wskazanego przez użytkownika pliku tekstowego i zapisuje wynik do dwóch plików tekstowych: w pierwszym zapisuje zliczone produkty, a w drugim zapisuje nadmiarowe sztuki lub produkty, których w ogóle nie powinno być.<br><br>
Format pliku nr 1 to KodKreskowy;NazwaZamówienia;DeklarowanaIlość:RzeczywistaIlość:<br>
4058823081665;APRIL;35;20<br>
4058823082792;JUNE;26;26<br>
4058823082822;JUNE;28;0<br><br>
Format pliku nr 2 to KodKreskowy;RzeczywistaIlość:<br>
4058823082792;5<br>
9818718199197;1
