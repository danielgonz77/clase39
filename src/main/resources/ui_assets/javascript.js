$( document ).ready(function() {
    console.log( "ready!" );

    // find elements on the page
    var banner = $("#banner-message");
    var button = $("#submit_button");
    var searchBox = $("#search_text");
    var resultsTable = $("#results table tbody");
    var resultsWrapper = $("#results");
    var noResultsError = $("#no_results_error");

    // handle search click
    button.on("click", function(){
        banner.addClass("alt");

        // send request to the server
        $.ajax({
        method : "POST",
        contentType: "application/json",
        data: createRequest(),
        url: "procesar_datos", 
        dataType: "json",
        success: onHttpResponse
        });
    });

    function createRequest() {
        var searchQuery = searchBox.val();

        // Search request to the server
        var frontEndRequest = {
            search_query: searchQuery,
        };

        return JSON.stringify(frontEndRequest);
    }

    function onHttpResponse(data, status) {
        if (status === "success" ) {
            console.log(data);
            addResults(data);
        } else {
            alert("Error connecting to the server " + status);
        }
    }

    /*
        Add results from the server to the html or how an error message
     */
    function addResults(data) {
        resultsTable.empty();

        //var score = data.response_score;
        //var title = data.response_title;
        //var time = data.response_time;
        //data.time
        noResultsError.hide();
        resultsWrapper.show();
        console.log(data[0]);


        for (let index = 0; index < data.length; index++){
            resultsTable.append("<tr><td>Libro: " + data[index].nombre+ 
                "</td></tr><tr><td> Puntuacion Final: " + data[index].score + 
                "</td></tr><tr><td> "  + "\t" + " </td></tr>" + 
                "</td></tr><tr><td> "  + "\t" + " </td></tr>" );
        }

        // data.array_fsp.forEach(element => {
        //     //console.log(element.frase);
        //     //console.log(element.frase[1]);

        //     resultsTable.append("<tr><td>Frase: " + element.Nombre+ 
        //         "</td></tr><tr><td> Ocurrencias: " + element.Score + 
        //         "</td></tr><tr><td> "  + "\t" + " </td></tr>" + 
        //         "</td></tr><tr><td> "  + "\t" + " </td></tr>" );

        //     // for (let index = 0; index < element.frase.length; index++) {
        //     //     //const element = array[index];
        //     //     // resultsTable.append("<tr><td>Frase: " + element.frase[index] + 
        //     //     // "</td></tr><tr><td> Ocurrencias: " + element.ocurrencias[index] + 
        //     //     // "</td></tr><tr><td> tf: " + element.tf[index] + 
        //     //     // "</td></tr><tr><td> itf: " + element.itf[index] + "</td></tr>" + 
        //     //     // "</td></tr><tr><td> Libro: " + element.libros[index] + "</td></tr>" + 
        //     //     // "</td></tr><tr><td> "  + "\t" + " </td></tr>" + 
        //     //     // "</td></tr><tr><td> "  + "\t" + " </td></tr>" );

        //     // }
        // });
        
        //resultsTable.append("<tr><td>Datos String: " + data  + "</td></tr>"); 

    }
});
