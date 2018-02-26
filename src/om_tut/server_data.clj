(ns om-tut.server-data
  )

(defonce movie-data 
  (atom 
    {
      :welcome-msg "Welcome to the top movies"
      :top-movies [
          {:id 1 :year 2000 :title "Title 1 in 2000"}
          {:id 2 :year 2000 :title "Title 2 in 2000"}
          {:id 3 :year 2000 :title "Title 3 in 2000"}
          {:id 4 :year 2001 :title "Title 1 in 2001"}
          {:id 5 :year 2001 :title "Title 2 in 2001"}
        ]
    }
    )
  )

(def import-movie-ratings [] nil)
(def import-movies [] nil)







