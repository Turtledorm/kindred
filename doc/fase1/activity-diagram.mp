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
    Activity.searchForOpponent("Procura", "adversário");
    Activity.selectOpponent("Seleciona", "adversário");
    Activity.serverMenu("Menu do", "servidor");
    Activity.createRoom("Cria", "sala");
    Activity.waitForOpponent("Espera", "adversário");
    Activity.roomMenu("Menu da", "sala");
    Activity.roomReady("Sala pronta", "para jogar");
    Activity.transitionToChosenMap("Transição para", "mapa escolhido");
    Activity.playersPositionUnits("Jogadores", "posicionam", "as unidades");
    Activity.startMatch("Começa a", "partida");
    Activity.startPlayerTurn("Início do", "turno do", "jogador");
    Activity.chooseUnit("Escolhe", "unidade");
    Activity.analyseTiles("Analisa para", "quais tiles", "pode se mover");
    Activity.moveUnit("Move", "unidade");
    Activity.choosePlayerUnit("Escolhe", "unidade", "aliada");
    Activity.chooseOpponentUnit("Escolhe", "unidade", "inimiga");
    Activity.analyseDamage("Analisa", "provável dano", "a ser causado");
    Activity.unitAttacks("Unidade aliada", "ataca");
    Activity.startOpponentTurn("Início do", "turno do", "oponente");
    Activity.opponentActions("Ações do", "oponente");
    Activity.closeRoomAndDisconnectPlayer("Fecha sala e", "desconecta jogador");
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
    leftToRight(hsep)(wantsToMove, chooseUnit);
    leftToRight(hsep)(wantsToAttack, choosePlayerUnit, chooseOpponentUnit,
                      analyseDamage);
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
    clink(transition)(wantsToAttack, wantsToEndTurn);
    clink(transition)(wantsToEndTurn, startOpponentTurn);
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
    link(transition)(pathStepX(closeRoomAndDisconnectPlayer.w, mainMenu.e,
                               -hsep/2));
    
    
    % labels
    item(iGuard)("quer jogar")
                (obj.w = userWantsToPlay.e + (0, -vsep/2));
    item(iGuard)("não quer jogar")
                (obj.c = (closeTheGame.se + mainMenu.sw)/2 + (0, -vsep));
    item(iGuard)("não quer hospedar partida")
                (obj.w = userWantsToHost.e + (-0.1*hsep, -0.7*vsep));
    item(iGuard)("quer hospedar partida")
                (obj.c = (clientMenu.nw + serverMenu.ne)/2 + (0, vsep));
    item(iGuard)("ambos aceitam")
                (obj.s = (usersAcceptMatch.c + transitionToChosenMap.w) / 2);
    item(iGuard)("a partida")
                (obj.n = (usersAcceptMatch.c + transitionToChosenMap.w) / 2);
    item(iGuard)("algum jogador não aceita a partida")
                (obj.w = usersAcceptMatch.ne + (0, 4 * vsep));
endfig;
end
