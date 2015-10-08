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
    Activity.mainMenu("Menu principal");
    Activity.closeTheGame("Fecha o jogo");
    Activity.gameMenu("Menu de jogos");
    Activity.clientMenu("Menu do cliente");
    Activity.searchForOpponent("Procura adversário");
    Activity.selectOpponent("Seleciona adversário");
    Activity.serverMenu("Menu do servidor");
    Activity.createRoom("Cria sala");
    Activity.waitForOpponent("Espera adversário");
    Activity.roomMenu("Menu da sala");
    Activity.roomReady("Sala pronta para jogar");
    
    % branches
    Branch.userWantsToPlay;
    Branch.userWantsToHost;
    Branch.usersAcceptMatch;
    
    % positioning
    topToBottom(vsep)(b, openTheGame, mainMenu, userWantsToPlay, gameMenu,
        userWantsToHost, clientMenu, searchForOpponent, selectOpponent);
    topToBottom(vsep)(serverMenu, createRoom, waitForOpponent);
    topToBottom(vsep)(e, closeTheGame);
    leftToRight(hsep)(closeTheGame, mainMenu);
    leftToRight(hsep)(serverMenu, clientMenu);
    roomMenu.n = (waitForOpponent.s + selectOpponent.s) / 2 - (0, 2 * vsep);
    topToBottom(vsep)(roomMenu, roomReady);
    usersAcceptMatch.w = (xpart selectOpponent.e + hsep, ypart roomMenu.c);

    % draw!
    drawObjects(b, e, openTheGame, mainMenu, userWantsToPlay, closeTheGame, gameMenu,
        userWantsToHost, clientMenu, searchForOpponent, selectOpponent,
        serverMenu, createRoom, waitForOpponent, roomMenu, roomReady,
        usersAcceptMatch);
    
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
    
    % labels
    item(iGuard)("quer jogar")
                (obj.w = userWantsToPlay.e + (0, -vsep/2));
    item(iGuard)("não quer jogar")
                (obj.c = (closeTheGame.se + mainMenu.sw)/2 + (0, -vsep));
    item(iGuard)("não quer hospedar partida")
                (obj.w = userWantsToHost.e + (0, -vsep/2));
    item(iGuard)("quer hospedar partida")
                (obj.c = (clientMenu.nw + serverMenu.ne)/2 + (0, vsep));

endfig;
end
