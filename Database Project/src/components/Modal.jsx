import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';

export default class ModalOpen extends Component {
    constructor(props) {
        super(props);
        this.state = { showModal: false };
        this.close = this.close.bind(this);
        this.open = this.open.bind(this);
        this.modalFooterImp = this.modalFooterImp.bind(this);
    }

    close() {
        this.setState({ page: false, showModal: false });
    }

    open() {
        this.setState({ showModal: true });
    }

    modalFooterImp() {
        let check = this.props.modalFooterFunc();
        if (check === 1 || check)
            this.close();
    }

    render() {

        let {modalBody, modalHeader, eventListener} = this.props;
        return (
            <div>
                <div onClick={this.open} >
                    {eventListener}
                </div>

                <Modal show={this.state.showModal} bsSize="large" onHide={this.close}>
                    <Modal.Header closeButton>
                        <Modal.Title>
                            {modalHeader}
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {modalBody}
                    </Modal.Body>
                </Modal>
            </div>
        );
    }
}


ModalOpen.propTypes = {
    eventListener: React.PropTypes.object,
    modalFooter: React.PropTypes.node,
    modalHeader: React.PropTypes.string,
    modalBody: React.PropTypes.node
};
