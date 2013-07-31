(ns game-of-life.core
  (:use game-of-life.grid
        game-of-life.ui)
  (:gen-class))

(defn -main [& args]
  (init-grid)
  (init-ui))
