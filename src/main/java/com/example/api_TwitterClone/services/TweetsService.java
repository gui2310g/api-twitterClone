package com.example.api_TwitterClone.services;

import com.example.api_TwitterClone.dto.TweetsDto;
import com.example.api_TwitterClone.entities.Tweets;
import com.example.api_TwitterClone.entities.Users;
import com.example.api_TwitterClone.mapper.TweetsMapper;
import com.example.api_TwitterClone.repositories.TweetsRepository;
import com.example.api_TwitterClone.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TweetsService {

    private final TweetsRepository tweetsRepository;

    private final UsersRepository usersRepository;

    private final TweetsMapper tweetsMapper;

    public TweetsDto createTweets(TweetsDto tweetsDTO, Integer userId) throws Exception {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new Exception("Cannot find a user to create a tweet"));

        Tweets tweets = tweetsMapper.toEntity(tweetsDTO);
        tweets.setUsers(users);
        Tweets savedTweet = tweetsRepository.save(tweets);

        return tweetsMapper.toDto(savedTweet);
    }

    public List<TweetsDto> findAllTweets() throws Exception {
        List<Tweets> tweets = tweetsRepository.findAll();

        if (tweets.isEmpty()) throw new Exception("No tweets found");

        return tweets.stream().map(tweetsMapper::toDto).toList();
    }

    public TweetsDto findTweetsById(Integer id) throws Exception {
       Tweets tweets  = tweetsRepository.findById(id)
               .orElseThrow(() -> new Exception("Could not find a tweet with this id"));

       return tweetsMapper.toDto(tweets);
    }

    public List<TweetsDto> searchTweets(String text) throws Exception {
        List<Tweets> tweets = tweetsRepository.findByTextContainingIgnoreCase(text);

        if (tweets.isEmpty()) throw new Exception("Could not find tweets with this text");

        return tweets.stream().map(tweetsMapper::toDto).toList();
    }

    public List<TweetsDto> findTweetsByUsersId(Integer userId) throws Exception {
        usersRepository.findById(userId).orElseThrow(() -> new Exception("Can't find a user with this id"));

        List<Tweets> tweets = tweetsRepository.findByUsersId(userId);

        if (tweets.isEmpty()) throw new Exception("No tweets found for this user");

        return tweets.stream().map(tweetsMapper::toDto).toList();
    }
}