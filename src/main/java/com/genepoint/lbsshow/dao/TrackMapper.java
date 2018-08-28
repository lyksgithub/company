package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.Track;
import com.genepoint.lbsshow.model.vo.TrackVO;

public interface TrackMapper {
	
	List<Track> getTrack(TrackVO trackVO);
	
	List<Track> getTrackByFloorAndTime(TrackVO trackVO);
	
	List<Track> getTrackByBuilding(TrackVO trackVO);
	
	List<Track> getPersonTrack(TrackVO trackVO);
}
