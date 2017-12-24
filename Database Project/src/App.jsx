import React, { Component } from 'react';
import './App.css';
import { Col, Grid, Row } from 'react-bootstrap';
import QueryBox from './components/QueryBox'

export default class AppContainer extends Component {

  render() {
    const {children} = this.props;
    return (
      <Grid fluid={true}>
        <Row>
          <div className="App">
            <Col xs={12}>
              {children}
            </Col>
          </div>
        </Row>
      </Grid>
    );
  }
}

AppContainer.propTypes = {
  children: React.PropTypes.node,
}

AppContainer.contextTypes = {
  router: React.PropTypes.object.isRequired
};