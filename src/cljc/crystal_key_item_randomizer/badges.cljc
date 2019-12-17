(ns crystal-key-item-randomizer.badges)

(def ordering {:ZEPHYRBADGE {:order 0}
               :HIVEBADGE {:order 1}
               :PLAINBADGE {:order 2}
               :FOGBADGE {:order 3}
               :STORMBADGE {:order 4}
               :MINERALBADGE {:order 5}
               :GLACIERBADGE {:order 6}
               :RISINGBADGE {:order 7}

               :BOULDERBADGE {:order 8}
               :CASCADEBADGE {:order 9}
               :THUNDERBADGE {:order 10}
               :RAINBOWBADGE {:order 11}
               :SOULBADGE {:order 12}
               :MARSHBADGE {:order 13}
               :VOLCANOBADGE {:order 14}
               :EARTHBADGE {:order 15}})

(def speedchoice-johto {:ZEPHYRBADGE {:address 426992 :value 27}
                        :HIVEBADGE {:address 1633200 :value 28}
                        :PLAINBADGE {:address 344157 :value 29}
                        :FOGBADGE {:address 630139 :value 30}
                        :STORMBADGE {:address 644690 :value 32}
                        :MINERALBADGE {:address 639314 :value 31}
                        :GLACIERBADGE {:address 1678273 :value 33}
                        :RISINGBADGE {:address 1658470 :value 34}})

(def speedchoice-kanto {:BOULDERBADGE {:address 1714314 :value 35}
                        :CASCADEBADGE {:address 1606750 :value 36}
                        :THUNDERBADGE {:address 1646809 :value 37}
                        :SOULBADGE {:address 1662993 :value 39}
                        :RAINBOWBADGE {:address 470232 :value 38}
                        :MARSHBADGE {:address 1612913 :value 40}
                        :VOLCANOBADGE {:address 1750307 :value 41}
                        :EARTHBADGE {:address 633417 :value 42}})

(def speedchoice
  ":value is the value passed to checkflag"
  (conj speedchoice-johto speedchoice-kanto))
