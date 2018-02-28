# IMDB Code Challenge

Code Challenge - Create a single page webapp to query IMDB for the top 100 movies for a certain year. The initial page should be a select form element with years 2000-2017. Once the year is selected, a list of the top 100 movies for that year should be displayed. The list should consist of links, which when clicked on, fetches the list of actors for that movie in a small panel on the page.

As a bonus, create a search box on the movie page to do a typeahead search for actor names.

## Overview

Goal: To apply Clojure and ClojureScript

## Setup

---STEP 1 - Getting Client Side up and running
To get an interactive development environment run:
    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:
    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:
    lein clean

To create a production build run:
    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

---STEP 2 - Getting Client Side (web api) up and running
In a separate terminal, change directory to the root of the project, then issue the following commands:
	lein repl
	(load-file "src/om_tut/server.clj")
	(def jetty (om-tut.server/start-server))
To stop the server, issue the following commands:
	(.stop jetty)
Unlike the cljs version in Step 1, the server side stuff doesn't have automatic live reloading.  So updating the code doesn't automatically update the server process.  I think that functionality exists, so i'll spend more time researching it later.

	


