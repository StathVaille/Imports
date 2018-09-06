shoppingList = [];

$( document ).ready(function() {
    //loadSuggestions();
    loadImportLocations();
    buildTable();
});

function loadImportLocations() {
    $.ajax({
        url : "/ui/importSource",
        type : "GET",
        success : function(data) {
            $("#import_source").html(data.systemName + ": " + data.stationName);
        },
        error : function(data) {
            $("#import_source").html("Error");
        }
    });

    $.ajax({
        url : "/ui/importDestination",
        type : "GET",
        success : function(data) {
            $("#import_destination").html(data.systemName + ": " + data.stationName);
        },
        error : function(data) {
            $("#import_destination").html("Error");
        }
    });
}

function loadSuggestions() {
    $('#animationImage').show();
    $('#suggestion_container').hide();
    $.ajax({
        url : "/api/import",
        type : "GET",
        success : function(data) {
            $('#animationImage').hide();
            $('#suggestion_container').show();
//            $("#suggestion_container").html("loaded");
            buildTable(data);
            console.log("Loaded suggestions");
        },
        error : function(data) {
            $('#animationImage').hide();
            $("#suggestion_container").html(data);
        }
    });
}

function buildTable() {
    $('#table').bootstrapTable({
        url: '/ui/imports',
        sortName: "profitPerDay",
        sortOrder: "desc",
        columns: [{
            field: 'icon',
            title: '',
            sortable: false,
            formatter: imageFormatter
        }, {
            field: 'itemName',
            title: 'Item',
            sortable: true
        }, {
            field: 'distinctMarketOrdersInDestination',
            title: '#Orders',
            sortable: true
        }, {
            field: 'buyPrice',
            title: 'Buy For',
            sortable: true,
            formatter: iskFormatter
        }, {
            field: 'salePrice',
            title: 'Sell For',
            sortable: true,
            formatter: iskFormatter
        }, {
            field: 'margin',
            title: 'Margin',
            sortable: true,
            formatter: marginFormatter
        }, {
            field: 'volRemainingInDestination',
            title: 'On Sale',
            sortable: true
        }, {
            field: 'numberSoldInDestinationPerDay',
            title: 'Sales/day',
            sortable: true,
            formatter: salesPerDayFormatter
        }, {
            field: 'profitPerDay',
            title: 'Profit/Day',
            sortable: true,
            formatter: iskFormatter
        }, {
            field: 'button',
            title: 'Add',
            sortable: false,
            formatter: buttonFormatter,
            events: operateEvents
        }],
    });
}

function imageFormatter(row, index, field) {
    return '<img src="https://image.eveonline.com/Type/' + index.item.typeId + '_32.png">';
}

function iskFormatter(iskValue) {
    iskValue = iskValue.toFixed(2);
    iskValue = iskValue.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return iskValue + ' ISK';
}

function marginFormatter(margin) {
    margin = margin - 1;
    margin = margin * 100;
    margin = margin.toFixed(2);
    return margin +"%";
}

function salesPerDayFormatter(salesPerDay) {
    salesPerDay = salesPerDay.toFixed(2);
    salesPerDay = salesPerDay.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return salesPerDay;
}

function buttonFormatter(row, index, field) {
    return '<button type="button" class="btn btn-outline-primary add" href="javascript:void(0)">Add</button>'
}

window.operateEvents = {
    'click .add': function (e, value, importSuggestion, index) {
        shoppingList.push(importSuggestion);
    }
};