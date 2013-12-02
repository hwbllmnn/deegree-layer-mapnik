//$HeadURL: svn+ssh://aschmitz@wald.intevation.org/deegree/base/trunk/resources/eclipse/files_template.xml $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2011 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package de.hwbllmnn.deegree.layer.mapnik;

import static org.slf4j.LoggerFactory.getLogger;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapnik.Box2d;
import mapnik.Image;
import mapnik.MapDefinition;
import mapnik.Renderer;

import org.deegree.feature.FeatureCollection;
import org.deegree.layer.LayerData;
import org.deegree.rendering.r2d.context.RenderContext;
import org.slf4j.Logger;

/**
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: stranger $
 * 
 * @version $Revision: $, $Date: $
 */
public class MapnikLayerData implements LayerData {

    private static final Logger LOG = getLogger( MapnikLayerData.class );

    private Box2d box;

    private int width;

    private int height;

    private MapDefinition config;

    private String srs;

    public MapnikLayerData( Box2d box, int width, int height, MapDefinition config, String srs ) {
        this.box = box;
        this.width = width;
        this.height = height;
        this.config = config;
        this.srs = srs;
    }

    @Override
    public void render( RenderContext context ) {
        Image image = null;

        try {
            config.setSrs( getProjLine( srs ) );
            config.resize( width, height );
            config.zoomToBox( box );

            image = new Image( width, height );
            Renderer.renderAgg( config, image );
            byte[] contents = image.saveToMemory( "png" );
            BufferedImage img = ImageIO.read( new ByteArrayInputStream( contents ) );
            context.paintImage( img );
        } catch ( Exception e ) {
            LOG.warn( "Unable to render mapnik image: {}", e.getLocalizedMessage() );
            LOG.trace( "Stack trace: ", e );
        } finally {
            if ( image != null )
                image.dispose();
        }
    }

    // hack to get proj lines from srs, only works on unix systems & with simple epsg codes
    private static String getProjLine( String srs )
                            throws IOException {
        srs = "<" + srs.split( ":" )[1] + ">";

        // ASCII...
        BufferedReader in = new BufferedReader( new FileReader( "/usr/share/proj/epsg" ) );
        String s;
        while ( ( s = in.readLine() ) != null ) {
            if ( s.startsWith( srs ) ) {
                srs = s.substring( srs.length() + 1 );
                break;
            }
        }
        in.close();
        return srs;
    }

    @Override
    public FeatureCollection info() {
        return null;
    }

}
