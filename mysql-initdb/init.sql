-- Ce script est exécuté automatiquement par le conteneur MySQL
-- lors de sa première initialisation (quand le volume de données est vide).

-- 1. S'assurer que l'utilisateur 'root' sur 'localhost' a le bon mot de passe.
--    Ceci est une bonne pratique pour la cohérence.
ALTER USER 'root'@'localhost' IDENTIFIED BY '0000';

-- 2. Créer ou mettre à jour l'utilisateur 'root' pour qu'il puisse se connecter
--    depuis n'importe quel hôte ('%') avec le mot de passe '0000'.
--    Ceci est crucial pour permettre aux autres conteneurs Docker (comme Jenkins)
--    de se connecter à MySQL.
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '0000';

-- 3. Accorder tous les privilèges à l'utilisateur 'root' depuis n'importe quel hôte
--    sur toutes les bases de données.
--    ATTENTION : Pour un environnement de production, vous devriez être plus restrictif
--    et accorder des privilèges spécifiques à un utilisateur dédié à l'application.
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- 4. Recharger les tables de privilèges pour que les changements prennent effet immédiatement.
FLUSH PRIVILEGES;

-- Note : La base de données 'foyer' est créée automatiquement par la variable d'environnement
-- MYSQL_DATABASE: foyer dans docker-compose.yml, donc pas besoin de la créer ici.
