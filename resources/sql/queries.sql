-- Place your queries here. Docs available https://www.hugsql.org/

-- :name get-settings :? :*
SELECT * FROM settings

-- :name store-user :! :n
INSERT INTO users (username, password) VALUES (:username, :password)

-- :name find-user-by-username :? :1
SELECT * FROM users WHERE username = :username