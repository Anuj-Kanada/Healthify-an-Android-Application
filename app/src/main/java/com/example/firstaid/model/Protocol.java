package com.example.firstaid.model;

import com.example.firstaid.R;

public class Protocol {
    private final Page[] pages;

    public Protocol() {
        pages = new Page[11];

        pages[0] = new Page(R.string.page0,
                new Choice(R.string.page0_choice1, 2),
                new Choice(R.string.page0_choice2, 1));

        pages[1] = new Page(R.string.page1,
                new Choice(R.string.page1_choice1, 2),
                new Choice(R.string.page1_choice2, 9));

        pages[2] = new Page(R.string.page2,
                new Choice(R.string.page2_choice1, 3),
                new Choice(R.string.page2_choice2, 4));

        pages[3] = new Page(R.string.page3);

        pages[4] = new Page(R.string.page4,
                new Choice(R.string.page4_choice1, 5),
                new Choice(R.string.page4_choice2, 5));

        pages[5] = new Page(R.string.page5);

        pages[6] = new Page(R.string.page6);

        pages[7] = new Page(R.string.page7,
                new Choice(R.string.done_button_text, 8));

        pages[8] = new Page(R.string.page8);
    }

    public Page getPage(int pageNumber) {
        //out of bounds error catch
        if (pageNumber >= pages.length) {
            pageNumber = 0;
        }
        return pages[pageNumber];
    }
}
