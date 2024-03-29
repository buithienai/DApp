import React, { Component } from 'react';
import { PropTypes } from 'prop-types';

import { connect } from 'react-redux';
import {
  selectImageAction, searchMediaAction,
  selectVideoAction
} from '../actions/mediaActions';
import PhotosPage from '../components/PhotosPage';
import VideosPage from '../components/VideosPage';
import '../assets/css/main.css';
import App from './App';


export class MediaGalleryPage extends Component {
  constructor() {
    super();
    this.handleSearch = this.handleSearch.bind(this);
    this.handleSelectImage = this.handleSelectImage.bind(this);
    this.handleSelectVideo = this.handleSelectVideo.bind(this);
  }

  componentDidMount() {
    this.props.dispatch(searchMediaAction('rain'));
  }

  handleSelectImage(selectedImage) {
    this.props.dispatch(selectImageAction(selectedImage));
  }

  handleSelectVideo(selectedVideo) {
    this.props.dispatch(selectVideoAction(selectedVideo));
  }

  handleSearch(event) {
    event.preventDefault();
    if (this.query !== null) {
      this.props.dispatch(searchMediaAction(this.query.value));
      this.query.value = '';
    }
  }

  render() {
    const { images, selectedImage, videos, selectedVideo } = this.props;
    return (
      <App>
        <div className="container-fluid">
          {images && selectedImage ? <div>
            <input
              type="text"
              ref={ref => (this.query = ref)}
            />
            <input
              type="submit"
              className="btn btn-primary"
              value="Search Library"
              onClick={this.handleSearch}
            />
            <div className="row">
              <PhotosPage
                images={images}
                selectedImage={selectedImage}
                onHandleSelectImage={this.handleSelectImage}
              />
              <VideosPage
                videos={videos}
                selectedVideo={selectedVideo}
                onHandleSelectVideo={this.handleSelectVideo}
              />
            </div>
          </div> : 'loading ....'}
        </div>
      </App>
    );
  }
}

MediaGalleryPage.propTypes = {
  images: PropTypes.array,
  selectedImage: PropTypes.object,
  videos: PropTypes.array,
  selectedVideo: PropTypes.object,
  dispatch: PropTypes.func.isRequired
};

/* Subscribe component to redux store and merge the state into component\s props */
const mapStateToProps = ({ images, videos }) => ({
  images: images[0],
  selectedImage: images.selectedImage,
  videos: videos[0],
  selectedVideo: videos.selectedVideo
});

/* connect method from react-router connects the component with redux store */
export default connect(
  mapStateToProps)(MediaGalleryPage);
