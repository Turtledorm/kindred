input metauml;
beginfig(1);
    
    hsep := 40;
    vsep := 20;
    
    % start
    Begin.b;
    
    % end
    End.e;
    
    % activities
    Activity.openTheGame("Abre o jogo");
    Activity.mainMenu("Menu", "principal");
    Activity.closeTheGame("Fecha", "o jogo");
    Activity.gameMenu("Menu de", "jogos");
    Activity.clientMenu("Menu do", "cliente");
    Activity.searchForOpponent("Procura", "advers�rio");
    Activity.selectOpponent("Seleciona", "advers�rio");
    Activity.serverMenu("Menu do", "servidor");
    Activity.createRoom("Cria", "sala");
    Activity.waitForOpponent("Espera", "advers�rio");
    Activity.roomMenu("Menu da", "sala");
    Activity.roomReady("Sala pronta", "para jogar");
    Activity.transitionToChosenMap("Transi��o para", "mapa escolhido");
    Activity.playersPositionUnits("Jogadores", "posicionam", "as unidades");
    Activity.startMatch("Come�a a", "partida");
    Activity.startPlayerTurn("In�cio do", "turno do", "jogador A");
    Activity.chooseUnit("Escolhe", "unidade");
    Activity.analyseTiles("Analisa para", "quais tiles", "pode se mover");
    Activity.moveUnit("Move", "unidade");
    Activity.choosePlayerUnit("Escolhe", "unidade", "aliada");
    Activity.chooseOpponentUnit("Escolhe", "unidade", "inimiga");
    Activity.analyseDamage("Analisa", "prov�vel dano", "a ser causado");
    Activity.unitAttacks("Unidade aliada", "ataca");
    Activity.startOpponentTurn("In�cio do", "turno do", "jogdador B");
    Activity.opponentActions("A��es do", "jogador B");
    Activity.closeRoomAndDisconnectPlayer("Fecha sala e", "desconecta jogadores");
    Activity.declarePlayerWinner("Declara", "vencedor");
    Activity.gameOver("Fim de", "jogo");
    
    
    
    
    % branches
    Branch.userWantsToPlay;
    Branch.userWantsToHost;
    Branch.usersAcceptMatch;
    Branch.wantsToMove;
    Branch.confirmsMove;
    Branch.wantsToAttack;
    Branch.confirmsAttack;
    Branch.playerDestroyedAll;
    Branch.wantsToEndTurn;
    Branch.opponentDestroyedAll;
    
    
    
    % positioning
    big_vsep := 2 * vsep;
    big_hsep := 2 * hsep;
    huge_vsep := 6 * vsep;
    topToBottom(vsep)(b, openTheGame, mainMenu, userWantsToPlay, gameMenu,
        userWantsToHost, clientMenu, searchForOpponent, selectOpponent);
    topToBottom(vsep)(serverMenu, createRoom, waitForOpponent);
    topToBottom(vsep)(e, closeTheGame);
    leftToRight(hsep)(closeTheGame, mainMenu);
    leftToRight(hsep)(serverMenu, clientMenu);
    roomMenu.n = (waitForOpponent.s + selectOpponent.s) / 2 - (0, 2 * vsep);
    topToBottom(vsep)(roomMenu, roomReady);
    usersAcceptMatch.w = (xpart selectOpponent.e + hsep, ypart roomMenu.c);
    leftToRight(big_hsep)(usersAcceptMatch, transitionToChosenMap);
    leftToRight(hsep)(transitionToChosenMap, playersPositionUnits);
    leftToRight(hsep)(playersPositionUnits, startMatch);
    bottomToTop(vsep)(playersPositionUnits, startPlayerTurn);
    bottomToTop(big_vsep)(startPlayerTurn, wantsToMove);
    bottomToTop(huge_vsep)(wantsToMove, wantsToAttack);
    bottomToTop(big_vsep)(wantsToAttack, wantsToEndTurn);
    bottomToTop(big_vsep)(wantsToEndTurn, startOpponentTurn);
    leftToRight(big_hsep)(wantsToMove, chooseUnit);
    leftToRight(big_hsep)(wantsToAttack, choosePlayerUnit);
    leftToRight(hsep)(choosePlayerUnit, chooseOpponentUnit, analyseDamage);
    rightToLeft(hsep)(confirmsAttack, unitAttacks, playerDestroyedAll);
    topToBottom(vsep)(choosePlayerUnit, playerDestroyedAll);
    leftToRight(hsep)(declarePlayerWinner, gameOver);
    leftToRight(hsep)(startOpponentTurn, opponentActions, opponentDestroyedAll);
    declarePlayerWinner.c = (xpart closeRoomAndDisconnectPlayer.c,
                              ypart mainMenu.c);
    leftToRight(hsep)(openTheGame, closeRoomAndDisconnectPlayer);
    leftToRight(hsep)(chooseUnit, analyseTiles, confirmsMove, moveUnit);


    % draw!
    drawObjects(b, e, openTheGame, mainMenu, userWantsToPlay, closeTheGame, gameMenu,
        userWantsToHost, clientMenu, searchForOpponent, selectOpponent,
        serverMenu, createRoom, waitForOpponent, roomMenu, roomReady,
        usersAcceptMatch, transitionToChosenMap, playersPositionUnits, startMatch,
        closeRoomAndDisconnectPlayer, declarePlayerWinner, gameOver, startPlayerTurn,
        startOpponentTurn, opponentActions, opponentDestroyedAll, wantsToMove,
        chooseUnit, analyseTiles, confirmsMove, moveUnit, wantsToAttack, 
        confirmsAttack, playerDestroyedAll, choosePlayerUnit, chooseOpponentUnit, 
        analyseDamage, unitAttacks, wantsToEndTurn);
    
    
    % links
    clink(transition)(b, openTheGame);
    clink(transition)(openTheGame, mainMenu);
    clink(transition)(mainMenu, userWantsToPlay);
    link(transition)(pathManhattanX(userWantsToPlay, closeTheGame));
    clink(transition)(closeTheGame, e);
    clink(transition)(userWantsToPlay, gameMenu);
    clink(transition)(gameMenu, userWantsToHost);
    clink(transition)(userWantsToHost, clientMenu);
    clink(transition)(clientMenu, searchForOpponent);
    clink(transition)(searchForOpponent, selectOpponent);
    link(transition)(pathManhattanX(userWantsToHost, serverMenu));
    clink(transition)(serverMenu, createRoom);
    clink(transition)(createRoom, waitForOpponent);
    link(transition)(pathStepY(waitForOpponent.s, roomMenu.n, -vsep));
    link(transition)(pathStepY(selectOpponent.s, roomMenu.n, -vsep));
    clink(transition)(roomMenu, roomReady);
    link(transition)(pathStepX(roomReady.e, usersAcceptMatch.w, hsep));
    link(transition)(pathManhattanY(usersAcceptMatch.n, userWantsToHost.e));
    clink(transition)(usersAcceptMatch, transitionToChosenMap);
    clink(transition)(transitionToChosenMap, playersPositionUnits);
    clink(transition)(playersPositionUnits, startMatch);
    link(transition)(pathStepY(startMatch.n, startPlayerTurn.s, vsep/2));
    clink(transition)(startPlayerTurn, wantsToMove);
    clink(transition)(wantsToMove, chooseUnit);
    clink(transition)(chooseUnit, analyseTiles);
    clink(transition)(analyseTiles, confirmsMove);
    clink(transition)(confirmsMove, moveUnit);
    link(transition)(pathStepY(confirmsMove.s, wantsToMove.s,
                               -vsep + ypart moveUnit.s - ypart wantsToMove.s));
    link(transition)(pathStepY(moveUnit.s, wantsToMove.s, -vsep));
    clink(transition)(wantsToMove, wantsToAttack);
    link(transition)(pathStepX(wantsToAttack.sw, wantsToMove.w, - 1.2 * hsep));
    clink(transition)(wantsToAttack, wantsToEndTurn);
    clink(transition)(wantsToEndTurn, startOpponentTurn);
    link(transition)(pathStepX(wantsToEndTurn.w, wantsToAttack.nw, -hsep));
    clink(transition)(startOpponentTurn, opponentActions);
    clink(transition)(wantsToAttack, choosePlayerUnit);
    clink(transition)(choosePlayerUnit, chooseOpponentUnit);
    clink(transition)(chooseOpponentUnit, analyseDamage);
    link(transition)(pathManhattanY(analyseDamage.s, confirmsAttack.e));
    clink(transition)(confirmsAttack, unitAttacks);
    clink(transition)(unitAttacks, playerDestroyedAll);
    link(transition)(pathManhattanX(playerDestroyedAll.w, wantsToAttack.s));
    link(transition)(pathStepY(playerDestroyedAll.s, gameOver.s, -vsep));
    link(transition)(pathStepY(confirmsAttack.s, wantsToAttack.s, -big_vsep));
    link(transition)(pathStepX(opponentDestroyedAll.e, startPlayerTurn.e, 
        xpart moveUnit.e + hsep/2 - xpart opponentDestroyedAll.e));
    link(transition)(pathStepY(opponentDestroyedAll.n, gameOver.n, big_vsep));
    clink(transition)(opponentActions, opponentDestroyedAll);
    clink(transition)(gameOver, declarePlayerWinner);
    clink(transition)(declarePlayerWinner, closeRoomAndDisconnectPlayer);
    
    draw (playerDestroyedAll.s) -- (playerDestroyedAll.s + (0, -vsep)) 
                                -- (xpart gameOver.c, ypart playerDestroyedAll.s
                                                        - vsep)
                                -- (gameOver.s) withcolor .6white;
    
    link(transition)(pathStepX(closeRoomAndDisconnectPlayer.w, mainMenu.e,
                               -hsep/2));
    
    
    % labels
    item(iGuard)("quer jogar")
                (obj.w = userWantsToPlay.e + (0, -vsep/2));
                
    item(iGuard)("n�o quer jogar")
                (obj.c = (closeTheGame.se + mainMenu.sw)/2 + (0, -vsep));
                
    item(iGuard)("n�o quer hospedar partida")
                (obj.w = userWantsToHost.e + (-0.1*hsep, -0.7*vsep));
                
    item(iGuard)("quer hospedar partida")
                (obj.c = (clientMenu.nw + serverMenu.ne)/2 + (0, vsep));
                
    item(iGuard)("ambos aceitam")
                (obj.s = (usersAcceptMatch.c + transitionToChosenMap.w) / 2);
    item(iGuard)("a partida")
                (obj.n = (usersAcceptMatch.c + transitionToChosenMap.w) / 2);
                
    item(iGuard)("algum jogador n�o aceita a partida")
                (obj.w = usersAcceptMatch.ne + (0, 4 * vsep));
                
    item(iGuard)("quer mover")
                (obj.s = (wantsToMove.e + chooseUnit.w) / 2);
    item(iGuard)("alguma unidade")
                (obj.n = (wantsToMove.e + chooseUnit.w) / 2);
    
    item(iGuard)("n�o quer mover unidade")
                (obj.w = wantsToMove.n + (0, 0.7 * vsep));
    
    item(iGuard)("confirma")
                (obj.s = 0.6 * confirmsMove.e + 0.4 * moveUnit.w);
    item(iGuard)("movimento")
                (obj.n = 0.6 * confirmsMove.e + 0.4 * moveUnit.w);
    
    item(iGuard)("cancela")
                (obj.e = confirmsMove.s - (0, 0.7 * vsep));
    item(iGuard)("movimento")
                (obj.w = confirmsMove.s - (0, 0.7 * vsep));
    
    item(iGuard)("confirma")
                (obj.s = 0.6 * confirmsAttack.w + 0.4 * unitAttacks.e);
    item(iGuard)("ataque")
                (obj.n = 0.6 * confirmsAttack.w + 0.4 * unitAttacks.e);
    
    item(iGuard)("cancela ataque")
                (obj.w = confirmsAttack.s - (0, vsep));
                
    item(iGuard)("n�o destruiu todas as")
                (obj.s = 0.5 * (xpart wantsToAttack.c, ypart playerDestroyedAll.w)
                       + 0.5 * playerDestroyedAll.w);
    item(iGuard)("unidades do oponente")
                (obj.n = 0.5 * (xpart wantsToAttack.c, ypart playerDestroyedAll.w)
                       + 0.5 * playerDestroyedAll.w);
    
    offset := ypart playerDestroyedAll.c - ypart playerDestroyedAll.s;
    item(iGuard)("destruiu todas as")
                (obj.s = 0.5 * (xpart wantsToAttack.c, ypart playerDestroyedAll.w)
                       + 0.5 * playerDestroyedAll.w + (0, -vsep - offset));
    item(iGuard)("unidades do oponente")
                (obj.n = 0.5 * (xpart wantsToAttack.c, ypart playerDestroyedAll.w)
                       + 0.5 * playerDestroyedAll.w + (0, -vsep - offset));
                       
    item(iGuard)("quer")
                (obj.s = (wantsToAttack.e + choosePlayerUnit.w) / 2);
    item(iGuard)("atacar")
                (obj.n = (wantsToAttack.e + choosePlayerUnit.w) / 2);
    
    item(iGuard)("n�o quer atacar nem mover")
                (obj.w = wantsToAttack.n + (0, 0.7 * vsep));

    item(iGuard)("quer voltar a")
                (obj.w = wantsToAttack.nw - (1.2 * hsep, 1.05 * vsep));
    item(iGuard)("mover unidades")
                (obj.w = wantsToAttack.nw - (1.2 * hsep, 1.40 * vsep));

    item(iGuard)("encerra turno")
                (obj.w = wantsToEndTurn.n + (0, 0.7 * vsep));
    
    item(iGuard)("n�o encerra")
                (obj.w = wantsToEndTurn.nw - (hsep, 0.85 * vsep));
    item(iGuard)("turno")
                (obj.w = wantsToEndTurn.nw - (hsep, 1.20 * vsep));
    
    item(iGuard)("n�o destruiu todas as")
                (obj.sw = opponentDestroyedAll.e + (0.1 * hsep, 0));
    item(iGuard)("unidades advers�rias")
                (obj.nw = opponentDestroyedAll.e + (0.1 * hsep, 0));
    
    item(iGuard)("destruiu todas as unidades advers�rias")
                (obj.w = opponentDestroyedAll.ne + (0, vsep));
endfig;
end
