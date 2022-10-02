# Projet_Hashiwokakero_Graph

Le Hashiwokakero, du japonais Hashi o kakero, construire des ponts est un 
jeu de logique inventé et publié par l'éditeur japonais NiKoli, qui est aussi à 
l'origine du Sudoku. Le but de ce projet est d'élaborer un programme capable de 
résoudre de tels problèmes.
Ce jeu se joue sur une grille rectangulaire de taille arbitraire. On y retrouve des 
nombres de 1 à 8 inclusivement, encerclés, nommés îles. Le but du jeu est de 
relier les îles entre elles par des ponts de façon à pouvoir relier n’importe quelle 
île à n’importe quelle autre. Ces ponts doivent respecter quelques contraintes :
● Tout pont débute et finit sur une île;
● Le nombre de ponts qui aboutissent à une île est le nombre indiqué
● sur l’île;
● Il ne peut y avoir plus de deux ponts entre deux îles;
● Chaque pont doit être un segment horizontal ou vertical.


MODÉLISATION DU JEU SOUS FORME D’UN GRAPHE 
Les îles représentent les sommets et les ponts, les arêtes.
Le nombre indiqué sur chaque île est le degré du sommet correspondant : le 
nombre maximal d'arêtes à relier à ce sommet.
Tous les sommets du graphe sont reliés entre eux de façon à trouver un graphe 
connexe (une seule composante connexe) : C’est la solution du jeu.
Si le degré d’un sommet est inférieur ou supérieur au nombre indiqué sur ce 
sommet, alors la solution est erronée.
Si le graphe trouvé possède plus d’une composante connexe alors, il n'y a pas de 
solution

II. NOTICE D’UTILISATION DU JEU
L’application se présente comme suit:
Une grille rectangulaire de taille prédéfinie est générée avec des sommets 
(nombre aléatoire) contenant de numéros aléatoires.
Un clic gauche sur un sommet dans une direction donnée (est, ouest, nord et sud) 
permet de le lier avec son voisin le plus proche dans cette direction. Ceci crée un 
lien double au cas où un lien simple existait déjà à la direction cliquée. 
Pour supprimer un lien entre deux sommets, il suffit de faire un clic droit sur ce 
sommet.
Le formulaire présente également un bouton permettant de quitter le jeu et un 
autre pour réinitialiser le jeu
