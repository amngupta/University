import express from 'express';
import morgan from 'morgan';
import path from 'path';
import queryEngine from './queryEngine/queryEngine';


const app = express();

// Setup logger
app.use(morgan(':remote-addr - :remote-user [:date[clf]] ":method :url HTTP/:http-version" :status :res[content-length] :response-time ms'));

// Serve static assets
app.use(express.static(path.resolve(__dirname, '..', 'build')));

app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

app.get('/query/:request', (req, res) => {
    let qE = new queryEngine();
    qE.performQuery(req.params.request)
        .then(response => {
            res.status(200).send(response);
        })
        .catch(err => {
            res.status(400).send(err);
        });
    // console.log(req.query);

});

// Always return the main index.html, so react-router render the route in the client
app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname, '..', 'build', 'index.html'));
});

module.exports = app;