import React, { useState, useRef, useEffect } from "react";
import { model } from "../utils/model";

/**
 * Main class for the table component.
 *
 * Design decision:
 * 1. Use virtual DOM to only render 10 rows at a time (or whatever calculation is)
 *    and redraw rows upon scrolling
 * 2. Sorting relies on backend fetch because Javascript does not handle sorting
 *    well, including merge sort. Backend (Java) hadles this a lot better
 * 3. Searching relies on backend fetch as well to reduce the already-complicated
 *    state management
 *
 * @param {{data: Object[], itemHeight: int, viewportHeight: int}} props
 * @returns {Object} the table element
 * @constructor
 */
export const TableComponent = (props) => {
  const [route, setRoute] = useState("/stats");
  const [viewData, setViewData] = useState(props.data);
  const [searchTerm, setSearchTerm] = useState("");
  const numVisibleItems = Math.trunc(props.viewPortHeight / props.itemHeight);
  const [viewPortHeight, setViewPortHeight] = useState({
    height: props.data.length * props.itemHeight,
  });

  const [startIndex, setStartIndex] = useState(0);
  const [endIndex, setEndIndex] = useState(numVisibleItems);
  const viewPortRef = useRef(true);
  const [sort, setSort] = useState({
    id: null,
    order: false
  });

  // rows to be displayed in the viewport
  const [rows, setRows] = useState(renderRows({
    viewData: viewData,
    start: startIndex,
    end: endIndex,
    itemHeight: props.itemHeight,
    numVisibleItems: numVisibleItems}));

  // used for skipping useEffect on initial mounting
  const isInitialMount = useRef(true);

  // adjust viewport and row index on data change
  useEffect(() => {
    setEndIndex(viewData.length >= numVisibleItems ? startIndex + numVisibleItems : viewData.length);
    setViewPortHeight({height: viewData.length * props.itemHeight});
  }, [viewData, numVisibleItems]);

  // re-render rows if any of view properties has been updated
  useEffect(() => {
    setRows(renderRows({
      viewData: viewData,
      start: startIndex,
      end: viewData.length >= numVisibleItems ? endIndex : (endIndex > 0 ? endIndex-1 : -1),
      itemHeight: props.itemHeight}));
  }, [startIndex, endIndex, sort, viewData, searchTerm, route])

  // sorting event handler
  useEffect(() => {
    if (isInitialMount.current) {
      isInitialMount.current = false;
    } else {
      setRoute(`/stats?id=${sort.id}&order=${sort.order ? "ASC" : "DESC"}&nameText=${searchTerm}`);
    }
  }, [sort, searchTerm])

  // fetches view data from server
  useEffect(() => {
    const BASE_URL = `${process.env.REACT_APP_.API_BASE_URL}:${process.env.REACT_APP_.API_PORT}`;
    if (isInitialMount.current) {
      isInitialMount.current = false;
    } else {
      fetch(BASE_URL + route, {
        method: "GET",
        headers: {
          'Accept': "application/json"
        }
      })
      .then(response => {
        if (response.ok) {
          return response.json();
        }
      })
      .then(data => {
        setViewData(data);
      })
      .catch(err => {
        console.error(err);
      });
    }
  }, [route]);

  return (
      <div className="table">
        <input className="input-name"
               placeholder="Search player by name"
               onChange={(event) => {
                 setSearchTerm(event.target.value);}}/>
        <div>
          <button onClick={
            function downloadObjectAsJson(){
              var array = typeof viewData != 'object' ? JSON.parse(viewData) : viewData;
              var str = '';
              for (var i = 0; i < model.length; i++) {
                str += model[i] + (i < model.length -1 ? ',' : '\r\n');
              }
              for (var j = 0; j < array.length; j++) {
                var line = '';
                for (var index in array[j]) {
                  if (line !== '') line += ','
                  line += array[j][index].replace(/,/g, '');
                }
                str += line + '\r\n';
              }

              var dataStr = "data:text/csv;charset=utf-8," + str;
              var anchor = document.createElement('a');
              anchor.setAttribute("href", dataStr);
              anchor.setAttribute("download",  "results.csv");
              document.body.appendChild(anchor);
              anchor.click();
              anchor.remove();
            }
          }>Export</button>
        </div>
        <div className="headerContainer">
          <div className="th">
            {model.map(column => {
              return (
                  <div className="column"
                       id={column}
                       key={column}
                       onClick={() => {
                         if (column === "Yds" || column === "Lng" || column === "TD") {
                           setSort({id: column, order: !sort.order})
                         }
                       }}>
                    {column}
                  </div>
              );
            })}
          </div>
        </div>
        <div ref={viewPortRef}
             style={{height:props.viewPortHeight}}
             className="viewPort"
             onScroll={() => {
               let rowIndex = Math.trunc(viewPortRef.current.scrollTop / props.itemHeight);
               if (rowIndex !== startIndex) {
                 setStartIndex(rowIndex);
                 setEndIndex((rowIndex + numVisibleItems) >= viewData.length ?
                     viewData.length-1 : rowIndex + numVisibleItems)
               }
             }}>
          <div className="dataContainer" style={viewPortHeight}>
            {rows}
          </div>
        </div>
        <span>
          Note: sort order will persist until page is refreshed
        </span>
      </div>
  );
}

/**
 * Helper function for Rendering viewport rows.
 * @param {{start: int, end: int, viewData: Object[], itemHeight: int}} props
 * @returns {Object[]} rows to be rendered
 */
function renderRows(props) {
  let result = [];
  if (props.end >= 0 && props.end < props.viewData.length) {
    if (props.end === 0) {
      result.push(<Row key={"0"}
                       value={props.viewData[0]}
                       top={0}
                       itemHeight={props.itemHeight}/>);
    } else {
      for (let i=props.start;i<=props.end;i++){
        let item=props.viewData[i];
        result.push(<Row key={i}
                         value={item}
                         top={i*props.itemHeight}
                         itemHeight={props.itemHeight}/>);
      }
    }
  }
  return result;
}

/**
 * Renders a single row of data.
 * @param {{top: int, itemHeight: int, value: Object[]}} props
 * @returns {Object} a single row
 * @constructor
 */
function Row(props) {
  if (props.value !== undefined) {
    return (
        <div className="row"
             style={{top:props.top, height:props.itemHeight}}>
          {model.map(column => {
            return (
                <span className="column" key={Math.random()} >
                {props.value[column]}
              </span>);
          })}
        </div>
    );
  }
}
