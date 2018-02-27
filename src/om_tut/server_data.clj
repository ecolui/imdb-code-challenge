(ns om-tut.server-data)

;;IMPORTANT TODO: Data was imported from text files @ 
;;Later on, Create a full blown, Odata driven web api
;;so that the client can query the data directly via an odata url

;;List contains only actors born after 1930 that are still alive.
;;Records with no birthyear are omitted from the cache
(defonce actor-data
  (atom
    ;;NOTE: If the atom is empty, it could be that your
    ;;input is encoded as UTF-8 BOM, instead of just UTF-8.
    (read-string (slurp "resources/public/imdb_data/actors.txt"))
    ))

(defonce movie-data 
  (atom 
    {
      :welcome-msg "Welcome to the top movies"
      :top-movies 
        (read-string (slurp "resources/public/imdb_data/topmovies.txt"))        
    }))







