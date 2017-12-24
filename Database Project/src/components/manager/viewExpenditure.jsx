import React, { Component } from 'react';
let request = require('request-promise-native');
import { Grid, Row, Col, Modal, ListGroup, ControlLabel, FormControl, Button, ListGroupItem } from 'react-bootstrap';
import JsonTable from 'react-json-table';
import ModalOpen from '../Modal';
import Moment from 'react-moment';
import NewExpenditureForm from '../employee/newExpenditureForm';
import toastr from 'toastr';

export default class ViewExpenditures extends Component {

    constructor(props) {
        super(props);
        this.state = {
            errors: {},
            rows: [],
            aggregation: [],
        };
        this.doQuery = this.doQuery.bind(this);
    }

    componentWillMount() {
        console.log("Here");
        let query = "SELECT * from Expenditure";
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        let self = this;
        request(options)
            .then(function (body) {
                self.setState({
                    rows: body.rows
                });
                query = "SELECT COUNT(*) FROM Expenditure";
                options = {
                    uri: 'http://localhost:9000/query/' + encodeURI(query),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                request(options)
                    .then(function (body) {
                        console.log(body);
                        for (let i = 0; i < body.rows.length; i++) {
                            body.rows[i]["COUNT"] = body.rows[i]["COUNT(*)"];
                            delete body.rows[i]["COUNT(*)"];
                        }
                        self.setState({
                            aggregation: body.rows
                        });
                        toastr.success("Entries loaded successfully!");
                    })
                    .catch(function (err) {
                        toastr.error("Failed to load entries...");
                        console.error(err);
                    });
            })
            .catch(function (err) {
                console.error(err);
            });
    }

    doQuery(e) {
        e.preventDefault();
        let id = this.pid.value || null;


        let desc = this.desc.value || null;
        let type = this.type.value || null;
        let amt = this.amt.value || null;
        let rel = this.relation.value || null;
        let group = this.group.value || null;
        let aggr = this.aggr.value || null;

        let filter = [];
        let querySuffix = " FROM Expenditure e";
        if (desc) { filter.push("LOWER(e.description) LIKE LOWER('%" + desc + "%')"); }
        if (type) { filter.push("LOWER(e.type) LIKE LOWER('%" + type + "%')"); }
        if (amt && rel) { filter.push("e.amount" + rel + amt); }
        if (id) {
            let q = "(e.eid IN (SELECT ew.eid FROM ExpenditureWorker ew WHERE ew.pid=" + id + " AND e.eid=ew.eid)" +
                "OR e.eid IN (SELECT em.eid FROM ExpenditureManager em WHERE em.pid=" + id + " AND e.eid=em.eid))";
            filter.push(q);
        }

        if (filter.length !== 0) { querySuffix += " WHERE " + filter.join(" AND "); }

        let self = this;
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI("SELECT DISTINCT *" + querySuffix),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        request(options)
            .then(function (body) {
                console.log(body);
                self.setState({
                    rows: body.rows
                });
                let aggrQuery = "";
                switch (aggr) {
                    case "COUNT":
                        aggrQuery = aggr + "(*)";
                        break;
                    case "AVG":
                    case "MAX":
                    case "MIN":
                    case "SUM":
                        aggrQuery = aggr + "(E.AMOUNT)";
                        break;
                    default:
                        break;
                }
                let groupByCol = "";
                let queryGroupBy = "";
                if (group && group !== "NONE") {
                    if (group === "TYPE") {
                        groupByCol = "e.type, ";
                        queryGroupBy = " GROUP BY e.type";
                    }
                }
                let options = {
                    uri: 'http://localhost:9000/query/' + encodeURI("SELECT DISTINCT " + groupByCol + aggrQuery + querySuffix + queryGroupBy),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                request(options)
                    .then(function (body) {
                        console.log(body);
                        for (let i = 0; i < body.rows.length; i++) {
                            body.rows[i][aggr] = body.rows[i][aggrQuery];
                            delete body.rows[i][aggrQuery];
                        }
                        self.setState({
                            aggregation: body.rows
                        });
                        toastr.success("Search success!");
                    })
                    .catch(function (err) {
                        toastr.error("Search failed...");
                        console.error(err);
                    });
            })
            .catch(function (err) {
                console.error(err);
            });
    }


    render() {
        const columns = [
            "EID",
            "TYPE",
            "DESCRIPTION",
            {
                key: "EXPENDITURE_DATE", label: "DATE",
                cell: (item, columnKey) => {
                    return <Moment format="DD/MM/YYYY">{item.EXPENDITURE_DATE}</Moment>;
                }
            }, "AMOUNT"
        ];

        const button = (<Button bsStyle="success">Add New Expenditure </Button>);
        const newExpenditure = <NewExpenditureForm />;

        const formBody = (
            <div>
                <form className="form-horizontal" onSubmit={this.doQuery}>
                    <fieldset>
                        <ListGroup>
                            <ListGroupItem>
                                <div className="row">
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Project ID</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='number' name='name' ref={ref => this.pid = ref} placeholder='ID Number' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Description</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.desc = ref} placeholder='Description' className="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Type</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.type = ref} placeholder='Type' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Amount</label>
                                        <div className="col-sm-5 col-md-6">
                                            <input type='number' name='name' ref={ref => this.amt = ref} placeholder='Amount' className="form-control" />
                                        </div>
                                       <div className="col-sm-3 col-md-3">
                                            <FormControl componentClass="select" inputRef={ref => this.relation = ref}>
                                                <option value="=">{"="}</option>
                                                <option value=">=">{">="}</option>
                                                <option value="<=">{"<="}</option>
                                            </FormControl>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="form-group col-sm-6">
                                        <ControlLabel className="col-sm-4 col-md-3">Group By</ControlLabel>
                                        <div className="col-sm-8 col-md-9">
                                            <FormControl componentClass="select" inputRef={ref => this.group = ref}>
                                                <option value="NONE">None</option>
                                                <option value="TYPE">Type</option>
                                            </FormControl>
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <ControlLabel className="col-sm-4 col-md-3">Aggregation</ControlLabel>
                                        <div className="col-sm-8 col-md-9">
                                            <FormControl componentClass="select" inputRef={ref => this.aggr = ref}>
                                                <option value="COUNT">Count</option>
                                                <option value="AVG">Average Amount</option>
                                                <option value="MAX">Max Amount</option>
                                                <option value="MIN">Min Amount</option>
                                                <option value="SUM">Sum of Amounts</option>
                                            </FormControl>
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-12">
                                        <div className="form-group col-sm-12">
                                            <div className=" col-xs-12 text-center">
                                                <input type="submit" className="btn btn-success" value="Search" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </ListGroupItem>
                        </ListGroup>
                    </fieldset>
                </form>
            </div>
        )

        return (
            <Grid fluid={true}>
                <Row>
                     <Col xs={10}>
                        {formBody}
                    </Col>
                    <Col xs={2}>
                        <ModalOpen eventListener={button} modalBody={newExpenditure} />
                    </Col>
                </Row>
                <Row>
                    <Col xs={12}>
                        <div className="table-responsive">
                            <JsonTable className="table" rows={this.state.aggregation} />
                        </div>
                    </Col>
                    <Col xs={12}>
                        <div className="table-responsive">
                            <JsonTable className="table" columns={columns} rows={this.state.rows} />
                        </div>
                    </Col>
                </Row>
            </Grid>
        )
    }
}
