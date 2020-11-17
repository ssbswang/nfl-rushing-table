import './App.css';
import React from 'react';
import { TableComponent } from './components/Table';
import useFetch from "./utils/api";

export default function App() {

  const { data, loading, error } = useFetch(process.env.REACT_APP_.ENDPOINT_STAT);

  if (loading) {
    return (
        <div className="status">
          Loading application ...
        </div>
    );
  }
  if (error) {
    return (
        <div className="status">
          Something went wrong :(
        </div>
    );
  }

  return (
      <div className="App">
        <TableComponent data={data} itemHeight={25} viewPortHeight={250}/>
      </div>
  );
}