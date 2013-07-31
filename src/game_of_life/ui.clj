(ns game-of-life.ui
  (:use seesaw.core
        [seesaw.graphics :only [draw rect style]]
        [seesaw.mouse :only [location]]
        game-of-life.grid))

(def flag (atom false))

(defn gen-all [p q]
  (apply concat (for [i (range p)]
     (for [j (range q)]
       [i j]))))

(defn paint-at [grfx i j w h itm]
  (let [color (if (zero? itm)
                (style :foreground :grey :background :white)
                (style :foreground :grey :background :black))]
    (draw grfx (rect (* i w) (* j h) w h) color)))

(defn draw-all [wid grfx]
  (let [grid @grid
        v-num (count grid)
        h-num (count (grid 0))
        w (quot (width wid) h-num)
        h (quot (height wid) v-num)]
    (doseq [[i j] (gen-all v-num h-num)]
      (paint-at grfx i j w h ((grid i) j)))))

(defn canvas-click [e]
  (let [[x y] (location e)
        canvas (.getComponent e)
        w (width canvas)
        h (height canvas)]
    (if (not @flag) (grid-switch x y w h))
    (repaint! canvas)))

(defn clear-screen [e]
  (if (not @flag) (init-grid)))

(defn mouse-click [e]
  (let [button (.getComponent e)]
    (swap! flag not)
    (if @flag
      (text! button "Stop")
      (text! button "Start"))))

(defn init-ui []
  (let [b (button :id :start-button 
                  :text "Start")
        a (button :id :clear-button 
                   :text "Clear All")
        c (canvas :id :main-canvas 
                  :paint draw-all)
        f (frame :title "Demo"
                 :on-close :dispose
                 :content (vertical-panel :items [b a c])
                 :visible? true)]
    (init-grid)
    (listen b :mouse-clicked mouse-click)
    (listen a :mouse-clicked clear-screen)
    (listen c :mouse-clicked canvas-click)
    (timer
      (fn [& args] 
        (if @flag (update-grid))
        (repaint! (select f [:#main-canvas]))))))
