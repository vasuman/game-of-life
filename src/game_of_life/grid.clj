(ns game-of-life.grid)

(def h 50)
(def w 50)
(def grid (atom nil))

(defn init-grid []
  (reset! grid (vec (for [i (range h)] 
                      (vec (for [j (range w)] 0))))))

(defn map-grid [func grid]
  (vec (map-indexed (fn [i row] 
                  (vec (map-indexed (fn [j el] 
                                      (func grid i j)) row))) grid)))

(defn get-neighbours [i j] 
  (mapv #(map + %1 %2) 
        (repeat [i j]) 
        [[0 1] [1 0] [1 1] [-1 -1] [-1 0] [0 -1] [1 -1] [-1 1]]))

(defn get-grid [grid [p q]]
  (let [i (mod p (count grid))
        j (mod q (count (grid i)))]
    ((grid i) j)))

(defn grid-state-toggle [grid i j]
  (let [oval ((grid i) j)
        compl (if (= oval 1) 0 1)]
    (assoc grid i (assoc (grid i) j compl))))

(defn grid-switch [x y w h]
  (let [i (quot x (quot w (count (@grid 0))))
        j (quot y (quot h (count @grid)))]
    (swap! grid #(grid-state-toggle % i j))))

(defn update-cell [grid i j] 
  (let 
    [num-n (reduce +
                   (map #(get-grid grid %) 
                        (get-neighbours i j)))] 
    (cond 
      (= num-n 3) 1 
      (and (= ((grid i) j) 1) (= num-n 2)) 1
      :else 0)))

(defn update-grid []
  (swap! grid #(map-grid update-cell %)) @grid)
