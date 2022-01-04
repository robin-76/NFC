# NFC

- Année : M2 IWOCS
- Sujet : Mobile
- TP : n°2

## Auteurs

|Nom|Prénom|
|--|--|
| *GUYOMAR* | *Robin*|
| *BOURGEAUX* | *Maxence*|

# Description

Petite application mobile pour simuler une feuille d'émargement à l'aide du scan de puce NFC sur android.

## Liste des fonctionnalités 

* *Scan* <br/>
  Permet de scanner une carte étudiante disposant d'une puce NFC. Si l'étudiant est déjà enregistré 
  dans la base de données, l'horaire de début d'examen est mis à jour. Si l'heure de début a déjà été mise
  à jour, alors l'application le détecte et met à jour l'heure de fin. Si jamais l'utilisateur n'est 
  pas enregistré, une alerte apparaît permettant de renseigner le nom et prénom de l'étudiant.
  Dans les deux cas, même si l'on scan une troisième fois ou plus, l'heure ne sera plus mise à jour.
  
* *Ajouter un etudiant* <br/>
  Permet d'ajouter un étudiant dans la base de données.
  
* *Liste des étudiant* <br/>
  Permet d'afficher le nombre total d'étudiants enregistrés dans la base de données puis de les afficher
  dans un ListView.
  
* *Choix de l'examen* <br/>
Permet de choisir à l'aide d'un spinner l'examen courant (qui sera utilisé lors de l'impression pdf) et efface les anciennes heures de débuts et fins des étudiants. 
  
* *Ajouter un examen* <br/>
  Permet d'ajouter un examen dans la base de données.
  
* *Liste des examens* <br/>
  Permet d'afficher le nombre total d'examens enregistrés dans la base de données puis de les afficher
  dans un ListView.
  
* *Reset DB* <br/>
  Permet de réinitialiser totalement la base de données.
  
* *PDF* <br/>
Permet d'imprimer la feuille d'émargement selon l'examen sélectionné.
