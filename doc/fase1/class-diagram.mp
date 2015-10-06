input metauml;
beginfig(1);
    Class.Map("Mapa")()();
    Class.Tile("Tile")()();
    Class.Unit("Unidade")()();
    Class.Class_("Classe")()();
    Class.Weapon("Arma")()();
    Class.Game("Jogo")()();
    Class.Player("Jogador")()();
    Class.Online("Online")()();
    Class.Terrain("Terreno")()();
    
    leftToRight(40)(Map, Tile, Unit, Class_);
    topToBottom(40)(Tile, Terreno);
    topToBottom(80)(Map, Game);
    leftToRight(40)(Online, Game, Player);
    topToBottom(40)(Class_, Weapon);

    drawObjects(Map, Tile, Unit, Class_, Weapon, Game, Player, Online, Terreno);
    
    clink(aggregationUni)(Map,     Game);
    clink(compositionUni)(Online,  Game);
    clink(compositionUni)(Player,  Game);
    clink(aggregation   )(Tile,    Map );
    clink(aggregationUni)(Terreno, Tile);
    clink(aggregationUni)(Unit,    Tile);
    clink(aggregationUni)(Class_,  Unit);
    
    link(aggregationUni)(pathManhattanX(Weapon, Unit));
    

endfig;
end
