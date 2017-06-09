CREATE OR REPLACE FUNCTION getTamanhoViewBox(VARCHAR) RETURNS TEXT AS $$ 

	DECLARE 
             cidade ALIAS FOR $1; 
	     viewBox TEXT;
	     BEGIN
		  SELECT INTO viewBox CAST(ST_xmin(box2d(ST_Envelope(geom))) as varchar) || ' ' || 
                  CAST(ST_ymax(box2d(ST_Envelope(geom))) * -1 as varchar) || ' ' ||
                  CAST(ST_xmax(box2d(ST_Envelope(geom))) - ST_xmin(box2d(ST_Envelope(geom))) as varchar) || ' ' ||
                  CAST(ST_ymax(box2d(ST_Envelope(geom))) - ST_ymin(box2d(ST_Envelope(geom))) as varchar)
                  FROM municipio WHERE nome ilike cidade;
	          return viewBox;
	    END;
$$language plpgsql;