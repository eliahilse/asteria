package apoMario.level;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.apogames.ApoConstants;
import org.apogames.entity.ApoAnimation;


import apoMario.ApoMarioClassLoader;
import apoMario.ApoMarioComponent;
import apoMario.ApoMarioConstants;
import apoMario.ApoMarioImageContainer;
import apoMario.ai.ApoMarioAI;
import apoMario.entity.ApoMarioCannon;
import apoMario.entity.ApoMarioCannonCannon;
import apoMario.entity.ApoMarioCoin;
import apoMario.entity.ApoMarioDestructableWall;
import apoMario.entity.ApoMarioEnd;
import apoMario.entity.ApoMarioEnemy;
import apoMario.entity.ApoMarioEntity;
import apoMario.entity.ApoMarioFlower;
import apoMario.entity.ApoMarioGumba;
import apoMario.entity.ApoMarioKoopa;
import apoMario.entity.ApoMarioParticle;
import apoMario.entity.ApoMarioPlayer;
import apoMario.entity.ApoMarioQuestionmark;
import apoMario.entity.ApoMarioShell;
import apoMario.entity.ApoMarioWall;
import apoMario.game.ApoMarioPanel;
import apoMario.game.ApoMarioReplay;
import apoMario.game.ApoMarioReplayPlay;

/* full class content identical to attached source, with one added call in setAnalysis: */